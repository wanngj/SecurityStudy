package com.baizhi.util;


import java.util.Collection;
import java.util.Map;

public class NonUtil {
    public static boolean isNon(Object object) {
        boolean isnon = false;
        if (object == null) {
            return true;
        } else {
            if (object instanceof String) {
                String str = (String) object;
                if (str.trim().length() == 0) {
                    isnon = true;
                }
            } else if (object instanceof Collection) {
                Collection<?> collection = (Collection<?>) object;
                if (collection.size() == 0) {
                    isnon = true;
                }
            } else if (object instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) object;
                if (map.size() == 0) {
                    isnon = true;
                }
            } else if (object instanceof StringBuffer) {
                StringBuffer strbuf = (StringBuffer) object;
                if (strbuf.length() == 0) {
                    isnon = true;
                }
            } else if (object instanceof StringBuilder) {
                StringBuilder strbuf = (StringBuilder) object;
                if (strbuf.length() == 0) {
                    isnon = true;
                }
            } else if (object.getClass().isArray()) {
                Object[] o = (Object[]) object;
                if (o.length == 0) {
                    isnon = true;
                }
            }
            return isnon;
        }
    }

    public static boolean isNotNon(Object object) {
        return !isNon(object);
    }

    public static boolean allIsNon(Object... objs) {
        if (!isNon(objs)) {
            for (Object obj : objs) {
                if (isNotNon(obj)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean allIsNotNon(Object... objs) {
        if (isNon(objs)) {
            return false;
        } else {
            for (Object obj : objs) {
                if (isNon(obj)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isOrNon(Object... objs) {
        if (isNon(objs)) {
            return true;
        } else {
            for (Object obj : objs) {
                if (isNon(obj)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isOrNotNon(Object... objs) {
        if (!isNon(objs)) {
            for (Object obj : objs) {
                if (isNotNon(obj)) {
                    return true;
                }
            }
        }
        return false;
    }
}
