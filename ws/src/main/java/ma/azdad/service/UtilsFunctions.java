package ma.azdad.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import ma.azdad.model.GenericBean;

@Component
public class UtilsFunctions {
	protected final static Logger log = LoggerFactory.getLogger(UtilsFunctions.class);

	final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	final static DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String[] badgeTab = { "badge-success", "badge-danger", "badge-info", "badge-pink", "badge-warning", "badge-primary", "badge-purple", "badge-yellow" };
	private static String[] mapIconTab = { "http://maps.google.com/mapfiles/ms/micons/green-dot.png", "http://maps.google.com/mapfiles/ms/micons/red-dot.png", "http://maps.google.com/mapfiles/ms/micons/ltblue-dot.png", "http://maps.google.com/mapfiles/ms/micons/pink-dot.png", "http://maps.google.com/mapfiles/ms/micons/orange-dot.png", "http://maps.google.com/mapfiles/ms/micons/blue-dot.png",
			"http://maps.google.com/mapfiles/ms/micons/purple-dot.png", "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png" };

	public static String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

	public static String getMonthName(int month) {
		return MONTHS[month - 1];
	}

	public static File getResourceFile(String relativePath) {
		try {
			return new ClassPathResource(relativePath).getFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Integer getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}

	public static Integer getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	public static Integer getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static Date getMonthStartDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static Date getMonthEndDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static Date addSecondsToDate(Date date, int seconds) {
		Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	public static String formatName(String name) {
		if (name == null || name.isEmpty())
			return null;
		name = name.trim();
		name = name.toLowerCase();
		name = name.replaceAll("[^a-z ]", "");
		name = name.replaceAll("( )+", " ");
		if (name.isEmpty())
			return null;
		name = name.substring(0, 0) + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		int i = 0;
		for (Character c : name.toCharArray()) {
			if (c == ' ')
				name = name.substring(0, i + 1) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
			i++;
		}
		return name;
	}

	public static String formatTab(String[] tab) {
		String[] result = Arrays.copyOf(tab, tab.length);
		for (int i = 0; i < result.length; i++)
			result[i] = "'" + result[i] + "'";
		return Arrays.toString(result);
	}

	public static Long getDateDifference(Date endDate, Date startDate) {
		return TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());
	}

	public static Integer getDateDifferneceInMonths(Date startDate, Date endDate) {
		return new Interval(new DateTime(startDate), new DateTime(endDate)).toPeriod().getYears() * 12 + new Interval(new DateTime(startDate), new DateTime(endDate)).toPeriod().getMonths();
	}

	public static String getParameter(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}

	public static Integer getIntegerParameter(String name) {
		try {
			return Integer.valueOf(getParameter(name));
		} catch (Exception e) {
			return null;
		}
	}

	public static Double getDoubleParameter(String name) {
		try {
			return Double.valueOf(getParameter(name));
		} catch (Exception e) {
			return null;
		}
	}

	public static Boolean getBooleanParameter(String name) {
		return "true".equals(getParameter(name));
	}

	public static String getFormattedDuration(long seconds) {
		return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
	}

	public static String getFormattedDuration(Date startDate, Date endDate) {
		return getFormattedDuration((endDate.getTime() - startDate.getTime()) / 1000);
	}

	public static String getBadge(Integer index) {
		return badgeTab[index % badgeTab.length];
	}

	public static String getMapIcon(Integer index) {
		return mapIconTab[index % mapIconTab.length];
	}

	public static String getLetter(Integer index) {
		if (index >= 0 && index <= 25)
			return String.valueOf((char) (index + 65));
		return null;
	}

	public static Date getMinDate(List<Date> dateList) {
		if (dateList.isEmpty())
			return null;
		Collections.sort(dateList);
		return dateList.iterator().next();
	}

	public static Date getMaxDate(List<Date> dateList) {
		if (dateList.isEmpty())
			return null;
		Collections.sort(dateList);
		Collections.reverse(dateList);
		return dateList.iterator().next();
	}

	public static Date getMinDate(Date date1, Date date2) {
		return date1.compareTo(date2) == -1 ? date1 : date2;
	}

	public static Date getMaxDate(Date date1, Date date2) {
		return date1.compareTo(date2) == 1 ? date1 : date2;
	}

	public static String getFormattedSize(Long size) {
		if (size == null)
			return null;
		if (size < 0.01 * 1024)
			return size + " o";
		Double newSize = size / 1024.00;
		NumberFormat formatter = new DecimalFormat("#0.00");
		if (newSize < 0.01 * 1024)
			return formatter.format(newSize) + " Ko";
		newSize = newSize / 1024.00;
		return formatter.format(newSize) + " Mo";
	}

	public static Date getDate(String source) {
		try {
			return df.parse(source);
		} catch (Exception e) {
			return null;
		}

	}

	public static Date getDateTime(String source) {
		try {
			return dtf.parse(source);
		} catch (Exception e) {
			return null;
		}

	}

	public static String getFormattedDate(Date date) {
		if (date == null)
			return null;
		return df.format(date);
	}

	public static String getFormattedDateTime(Date date) {
		if (date == null)
			return null;
		return dtf.format(date);
	}

	public static Boolean contains(String key, String... values) {
		return Arrays.asList(values).contains(key);
	}

	public static String cleanString(String str) {
		if (str == null)
			return null;
		return str.trim().replaceAll("\\s+", " ");
	}

	public static String formatDouble(Double d) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(d);
	}

	public static Integer compareDates(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		if (c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR))
			return 1;
		if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR))
			return -1;
		if (c1.get(Calendar.MONTH) > c2.get(Calendar.MONTH))
			return 1;
		if (c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH))
			return -1;
		if (c1.get(Calendar.DAY_OF_MONTH) > c2.get(Calendar.DAY_OF_MONTH))
			return 1;
		if (c1.get(Calendar.DAY_OF_MONTH) < c2.get(Calendar.DAY_OF_MONTH))
			return -1;
		return 0;
	}

	public static Integer compareDoubles(Double x, Double y) {
		BigDecimal a = new BigDecimal(x).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal b = new BigDecimal(y).setScale(2, BigDecimal.ROUND_HALF_UP);
		return a.compareTo(b);
	}

	public static Integer compareDoubles(Double x, Double y, int scale) {
		BigDecimal a = new BigDecimal(x).setScale(scale, BigDecimal.ROUND_HALF_UP);
		BigDecimal b = new BigDecimal(y).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return a.compareTo(b);
	}

	public static double distFrom(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344 * 1000;
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static Boolean isValidEmail(String email) {
		if (email == null)
			return false;
		return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	}

	public static Boolean isValidPhoneNumber(String phone) {
		if (phone == null)
			return false;
		return phone.matches("[+]?[0-9]+");
	}

	public static String convertToPhoneNumber(String phone) {
		if (phone == null)
			return null;
		return phone.replaceAll("[^+0-9]", "");
	}

	public static String getCorrectFromList(String item, List<String> list) {
		for (String str : list)
			if (str.equalsIgnoreCase(item))
				return str;
		return null;
	}

	public static String sendHttpRequest(String method, String url, HashMap<String, String> params) {
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod(method);
			con.setDoInput(true);
			con.setDoOutput(true);
			// add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			String encoding = Base64.getEncoder().encodeToString("admin:admin".getBytes());
			con.setRequestProperty("Authorization", "Basic " + encoding);
			OutputStream os = con.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getPostDataString(params));
			writer.flush();
			writer.close();
			os.close();
			int responseCode = con.getResponseCode();
			log.info("\nSending '" + method + "' request to URL : " + url);
			log.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print result
			log.info(response.toString());
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	public static String path() {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return ctx.getRealPath("/");
	}

	public static String cutText(String text, int maxSize) {
		if (text == null)
			return null;
		return text.length() <= maxSize ? text : text.substring(0, maxSize) + "...";

	}

	public static String stringToMD5(String password) {
		String passwordMD5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md5.length; i++) {
				sb.append(Integer.toString((md5[i] & 0xFF) + 256, 16).substring(1));
			}
			passwordMD5 = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return passwordMD5;
	}

	public static String getChanges(GenericBean gb1, GenericBean gb2) {
		if (gb1 == null || gb2 == null || gb1 == gb2 || gb1.getClass() != gb2.getClass())
			return null;

		System.out.println(gb1.getClassName());

		String result = "";

		Field[] m = gb1.getClass().getDeclaredFields();

		for (int i = 0; i < m.length; i++)
			try {
				if (Collection.class.isAssignableFrom(m[i].getType()))
					continue;
				if (m[i].getName().startsWith("tmp"))
					continue;
				if (m[i].getName().toLowerCase().contains("password"))
					continue;
				String from, to;
				m[i].setAccessible(true);
				Object o1 = m[i].get(gb1);
				Object o2 = m[i].get(gb2);

				if (o1 == null && o2 == null)
					continue;
				if (o1 != null && o1.equals(o2))
					continue;

				to = o1 + "";
				from = o2 + "";

				if (o1 != null && o2 != null && o1 instanceof GenericBean && o2 instanceof GenericBean) {
					GenericBean ogb1 = ((GenericBean) o1);
					GenericBean ogb2 = ((GenericBean) o2);
					if (ogb1.id().equals(ogb2.id()))
						continue;

					try {
						to = ogb1.getIdentifierName();
					} catch (Exception e) {
						to = "#" + ogb1.getIdStr();
					}
					try {
						from = ogb2.getIdentifierName();
					} catch (Exception e) {
						from = "#" + ogb2.getIdStr();
					}
				}

				result += m[i].getName() + " from : " + from + ", to : " + to + "<br/>";

			} catch (Exception e) {
				e.printStackTrace();
			}

		return result;
	}

	public static Boolean validateEmail(String email) {
		try {
			new InternetAddress(email).validate();
			return true;
		} catch (AddressException e) {
			return false;
		}
	}

	public static String firstNotNullAndNotEmpty(String... params) {
		for (int i = 0; i < params.length; i++)
			if (params[i] != null && !params[i].isEmpty())
				return params[i];
		return null;
	}

	public static String generateQrKey() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Long getDateDifferenceInHours(Date endDate, Date startDate) {
		return TimeUnit.MILLISECONDS.toHours(endDate.getTime() - startDate.getTime());
	}

	public static Date addDays(Date date, Integer days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	public static Date getCurrentDate() {
		return new Date();
	}

	public static Integer getCurrentMonth() {
		return getMonth(getCurrentDate());
	}

	public static Integer getCurrentYear() {
		return getYear(getCurrentDate());
	}
}