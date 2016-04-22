package au.com.ifti;

import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Controller {
  public abstract void run(HttpServletRequest request, HttpServletResponse response, Matcher matcher);
}
