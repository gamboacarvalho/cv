package util;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static int getTeamIdFromUri(String href){
        int val = -1;
        try {
            val = Integer.valueOf(href.substring(href.lastIndexOf('/') + 1));
        }
        catch(NumberFormatException e){
            throw new RuntimeException(e);
        }
        return val;
    }

    public static int getIntValueFromRequestUri(HttpServletRequest req){
        return Integer.valueOf(req.getPathInfo().substring(1));
    }
}
