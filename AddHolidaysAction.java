/**
 * 
 */
package com.nacre.leave_management_system.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacre.leave_management_system.delegate.AddHolidayDelegate;
import com.nacre.leave_management_system.dto.HolidaysDTO;
import com.nacre.leave_management_system.exception.DatabaseException;
import com.nacre.leave_management_system.util.DateUtil;

/**
 * @author AjayKumarK and Sangram
 * this servlet is used as controller or action for add holidays
 *
 */
@WebServlet("/addholiday")
public class AddHolidaysAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HolidaysDTO hdto = null;
		HolidaysDTO isExist = null;
		AddHolidayDelegate addHolidayDelegate = null;
		String holiday_date_from = req.getParameter("holiday_date_from");
		String holiday_date_to = req.getParameter("holiday_date_to");
		//System.out.println(holiday_date_from + " " + holiday_date_to);
		String holiday_name = req.getParameter("hname");
		try {
			// if admin provided inputs are equal to null then it'll goes to
			// addmin page with appropriate message
			if (holiday_name != null && holiday_date_from != null && holiday_date_from != "" && holiday_name != "") {
				addHolidayDelegate = new AddHolidayDelegate();
				isExist = addHolidayDelegate.isHolidayExist(DateUtil.getSqlFromString$Type$YYYYMMDD(holiday_date_from),holiday_date_to);
				if (isExist != null) {
					req.setAttribute("error", holiday_date_from + " is alread exists for " + isExist.getHoliday_name());
					req.getRequestDispatcher("/jsp/admin/addholidays.jsp").forward(req, resp);
				} else {

					hdto = new HolidaysDTO();
					hdto.setHoliday_date_from(DateUtil.getSqlFromString$Type$YYYYMMDD(holiday_date_from));
					if (holiday_date_to != null && holiday_date_to != "")
						hdto.setHoliday_date_to(DateUtil.getSqlFromString$Type$YYYYMMDD((holiday_date_to)));
					hdto.setHoliday_name(holiday_name);

					if (addHolidayDelegate.saveHoliday(hdto)) {
						req.setAttribute("success", "successfully added");
						req.getRequestDispatcher("/jsp/admin/addholidays.jsp").forward(req, resp);
					} else {
						req.setAttribute("error", "sorry! internal problem try again");
						req.getRequestDispatcher("/jsp/admin/addholidays.jsp").forward(req, resp);
					}
				}
			} else {
				req.setAttribute("error", "please provide valid details");
				req.getRequestDispatcher("/jsp/admin/addholidays.jsp").forward(req, resp);
			}
		} catch (DatabaseException db) {
			db.printStackTrace();
		}

	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
