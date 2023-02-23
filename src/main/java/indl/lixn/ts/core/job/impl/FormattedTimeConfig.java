package indl.lixn.ts.core.job.impl;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.JobTimeConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lixn
 * @description
 * @date 2023/02/20 15:03
 **/
public class FormattedTimeConfig implements JobTimeConfig {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat DEFAULT_FORMATTER = new SimpleDateFormat(DEFAULT_FORMAT);

    private static final Map<String, SimpleDateFormat> formatterByTimeExpression = new ConcurrentHashMap<>();

    private long timestamp;

    static {
        formatterByTimeExpression.put(DEFAULT_FORMAT, DEFAULT_FORMATTER);
    }

    public FormattedTimeConfig(String formattedTimeStr) {
        this(formattedTimeStr, DEFAULT_FORMAT);
    }

    public FormattedTimeConfig(String formattedTimeStr, String dateFormat) {
        SimpleDateFormat formatter = getFormatter(dateFormat);
        try {
            Date date = formatter.parse(formattedTimeStr);
            if (date.before(new Date())) {
                // TODO 该怎么处理呢
                return;
            }
            this.timestamp = date.getTime();
        } catch (Exception ex) {
            throw new TsException("Parse error. Check that whether your timeStr and format is correct or not",
                    ex.getCause());
        }
    }

    @Override
    public long transformAsTimestamp() {
        return this.timestamp;
    }

    private SimpleDateFormat getFormatter(String dateFormat) {
        SimpleDateFormat formatter = formatterByTimeExpression.get(dateFormat);
        if (formatter == null) {
            formatterByTimeExpression.put(dateFormat, new SimpleDateFormat(dateFormat));
            formatter = formatterByTimeExpression.get(dateFormat);
        }
        return formatter;
    }
}
