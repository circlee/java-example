package com.example.jxls;

import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class JxlsUtil {

    public static Boolean export(InputStream input, OutputStream output, Map<String, Object> data) {
        try {
            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            Transformer transformer = jxlsHelper.createTransformer(input, output);
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functions = new HashMap<>();
            functions.put(JexlUtilFunction.definition, new JexlUtilFunction());
            functions.put(JexlDateFunction.definition, new JexlDateFunction());
            evaluator.getJexlEngine().setFunctions(functions);
            Context context = new Context();
            for (Map.Entry<String, Object> entry : data.entrySet())
                context.putVar(entry.getKey(), entry.getValue());
            jxlsHelper.processTemplate(context, transformer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
