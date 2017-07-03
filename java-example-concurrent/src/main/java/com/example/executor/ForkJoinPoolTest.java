package com.example.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolTest {

    public static void main(String[] args) throws Exception {
        ForkJoinPool executor = new ForkJoinPool(10);

        // testInvoke(executor);
        testInvoke2(executor);

        executor.shutdown();
    }

    private static void testInvoke(ForkJoinPool executor) throws Exception {
        PrintTask task = new PrintTask(1, 1000);

        /*
         * java.util.concurrent.RecursiveAction 接口没返回值
         */
        executor.invoke(task);
    }

    private static void testInvoke2(ForkJoinPool executor) throws Exception {
        SumTask task = new SumTask(1, 1000);

        /*
         * java.util.concurrent.RecursiveTask 接口有返回值
         */
        Integer sum = executor.invoke(task);
        System.out.println("sum: " + sum);
    }

    static class PrintTask extends RecursiveAction {

        private int x1;
        private int x2;

        public PrintTask(int x1, int x2) {
            this.x1 = x1;
            this.x2 = x2;
        }

        @Override
        protected void compute() {
            if (x2 - x1 > 10) {
                List<PrintTask> subtasks = new ArrayList<>();
                int m, m1, m2;
                if ((x2 - x1) % 2 == 0) {
                    m = (x2 - x1) / 2;
                    m1 = x1 + m - 1;
                    m2 = x2 - m;
                } else {
                    m = (x2 - x1 - 1) / 2;
                    m1 = x1 + m;
                    m2 = x2 - m;
                }
                subtasks.add(new PrintTask(x1, m1));
                subtasks.add(new PrintTask(m2, x2));

                for (PrintTask subtask : subtasks)
                    subtask.fork();
            } else {
                String[] strs = new String[x2 - x1 + 1];
                int i = 0;
                for (int x = x1; x <= x2; x++) {
                    strs[i++] = String.valueOf(x);
                }
                System.out.println("nums: " + String.join(" + ", strs));
            }
        }
    }

    static class SumTask extends RecursiveTask<Integer> {

        private int x1;
        private int x2;

        public SumTask(int x1, int x2) {
            this.x1 = x1;
            this.x2 = x2;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            if (x2 - x1 > 10) {
                List<SumTask> subtasks = new ArrayList<SumTask>();
                int m, m1, m2;
                if ((x2 - x1) % 2 == 0) {
                    m = (x2 - x1) / 2;
                    m1 = x1 + m - 1;
                    m2 = x2 - m;
                } else {
                    m = (x2 - x1 - 1) / 2;
                    m1 = x1 + m;
                    m2 = x2 - m;
                }
                subtasks.add(new SumTask(x1, m1));
                subtasks.add(new SumTask(m2, x2));

                for (SumTask subtask : subtasks)
                    subtask.fork();

                for (SumTask subtask : subtasks)
                    sum += subtask.join();
            } else {
                String[] strs = new String[x2 - x1 + 1];
                int i = 0;
                for (int x = x1; x <= x2; x++) {
                    strs[i++] = String.valueOf(x);
                    sum += x;
                }
                System.out.println("sum: " + String.join(" + ", strs) + " = " + sum);
            }
            return sum;
        }
    }

}
