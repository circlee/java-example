package com.example.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CanalHelper {

    final static String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    final static String TIME_REGEX = "\\d{2}:\\d{2}:\\d{2}";
    final static String DATE_TIME_REGEX = "\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}";
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    static SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static <T> List<T> getRows(CanalEntry.Entry entry, Class<T> clazz) {
        CanalEntry.RowChange rowChange = getRowChange(entry);
        return getRows(rowChange, clazz);
    }

    public static <T> List<T> getRows(CanalEntry.RowChange rowChange, Class<T> clazz) {
        List<T> rows = new ArrayList<T>();
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            T row = newInstance(clazz);
            for (int i = 0; i < rowData.getAfterColumnsCount(); i++) {
                setFieldValue(row, rowData.getAfterColumns(i).getName(), rowData.getAfterColumns(i).getValue());
            }
            rows.add(row);
        }
        return rows;
    }

    public static <T> List<T> getRawRows(CanalEntry.Entry entry, Class<T> clazz) {
        CanalEntry.RowChange rowChange = getRowChange(entry);
        return getRawRows(rowChange, clazz);
    }

    public static <T> List<T> getRawRows(CanalEntry.RowChange rowChange, Class<T> clazz) {
        List<T> rows = new ArrayList<T>();
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            T row = newInstance(clazz);
            for (int i = 0; i < rowData.getBeforeColumnsCount(); i++) {
                setFieldValue(row, rowData.getBeforeColumns(i).getName(), rowData.getBeforeColumns(i).getValue());
            }
            rows.add(row);
        }
        return rows;
    }

    public static boolean isDdl(CanalEntry.Entry entry) {
        CanalEntry.RowChange rowChange = getRowChange(entry);
        return isDdl(rowChange);
    }

    public static boolean isDdl(CanalEntry.RowChange rowChange) {
        return rowChange.getIsDdl();
    }

    public static boolean isQuery(CanalEntry.Entry entry) {
        CanalEntry.RowChange rowChange = getRowChange(entry);
        return isQuery(rowChange);
    }

    public static boolean isQuery(CanalEntry.RowChange rowChange) {
        return rowChange.getEventType() == CanalEntry.EventType.QUERY;
    }

    public static boolean isInsert(CanalEntry.Entry entry) {
        CanalEntry.RowChange rowChange = getRowChange(entry);
        return isInsert(rowChange);
    }

    public static boolean isInsert(CanalEntry.RowChange rowChange) {
        return rowChange.getEventType() == CanalEntry.EventType.INSERT;
    }

    public static boolean isUpdate(CanalEntry.Entry entry) {
        CanalEntry.RowChange rowChange = getRowChange(entry);
        return isUpdate(rowChange);
    }

    public static boolean isUpdate(CanalEntry.RowChange rowChange) {
        return rowChange.getEventType() == CanalEntry.EventType.UPDATE;
    }

    public static boolean isDelete(CanalEntry.Entry entry) {
        CanalEntry.RowChange rowChange = getRowChange(entry);
        return isDelete(rowChange);
    }

    public static boolean isDelete(CanalEntry.RowChange rowChange) {
        return rowChange.getEventType() == CanalEntry.EventType.DELETE;
    }

    public static CanalEntry.RowChange getRowChange(CanalEntry.Entry entry) {
        try {
            return CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void setFieldValue(Object object, String columnName, String columnValue) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String methodIgnoreCaseName = "set" + columnName.replaceAll("_", "");
            if (method.getName().equalsIgnoreCase(methodIgnoreCaseName) && method.getParameterTypes().length == 1) {
                setFieldValue(object, method, columnValue);
                break;
            }
        }
    }

    private static void setFieldValue(Object object, Method method, String columnValue) {
        try {
            Class<?> parameterType = method.getParameterTypes()[0];
            Object fieldValue = null;
            if (columnValue == null) {
                fieldValue = null;
            } else if (parameterType == String.class) {
                fieldValue = columnValue;
            } else if (parameterType == Integer.class && columnValue.length() > 0) {
                fieldValue = new Integer(columnValue);
            } else if (parameterType == Long.class && columnValue.length() > 0) {
                fieldValue = new Long(columnValue);
            } else if (parameterType == Float.class && columnValue.length() > 0) {
                fieldValue = new Float(columnValue);
            } else if (parameterType == Double.class && columnValue.length() > 0) {
                fieldValue = new Double(columnValue);
            } else if (parameterType == BigDecimal.class && columnValue.length() > 0) {
                fieldValue = new BigDecimal(columnValue);
            } else if (parameterType == Boolean.class && columnValue.length() > 0) {
                fieldValue = "1".equals(columnValue);
            } else if (parameterType == Date.class && columnValue.length() > 0) {
                fieldValue = toDate(columnValue);
            }
            method.invoke(object, fieldValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static Date toDate(String columnValue) {
        try {
            Date date = null;
            if (columnValue.matches(DATE_REGEX)) {
                date = dateFormat.parse(columnValue);
            } else if (columnValue.matches(TIME_REGEX)) {
                date = timeFormat.parse(columnValue);
            } else if (columnValue.matches(DATE_TIME_REGEX)) {
                date = datetimeFormat.parse(columnValue);
            }
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
