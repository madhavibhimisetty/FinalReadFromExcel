
package omniwyse.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import omniwyse.read.EmployeeAttendence;

public class ExtractEmployeeDataFromExcel {
	public static HashMap<Integer, Employee> extract() throws IOException, InvalidFormatException {
		XSSFWorkbook workSheet = null;
		HashMap<Integer, Employee> employeeMap = null;
		try {
			FileInputStream finXLSX = new FileInputStream(new File("src/main/resources/b.xlsx"));
			workSheet = new XSSFWorkbook(finXLSX);
			XSSFSheet sheet = workSheet.getSheetAt(0);
			Row row;
			Row dayRow = null;
			employeeMap = new HashMap<>();
			for (int i = 1; i <= sheet.getLastRowNum() - 2; i++) {
				row = (Row) sheet.getRow(i);
				String id1;
				id1 = row.getCell(1).toString();
				if (id1.trim().equals("Days")) {
					dayRow = row;
				}
				ArrayList<EmployeeAttendence> attendencelist = null;
				Employee employee = null;
				if (id1.trim().equals("Employee Code:-")) {
					employee = new Employee();
					employee.setCode(Integer.parseInt(row.getCell(10).toString()));
					employee.setName(row.getCell(24).toString());
					EmployeeAttendence attendence = null;
					attendencelist = new ArrayList<EmployeeAttendence>();
					for (int j = i + 1; j < sheet.getLastRowNum(); j++) {
						Row inTimeRow = sheet.getRow(j);
						String cells = inTimeRow.getCell(1).toString();
						if (cells.trim().equals("In Time")) {
							Row outTimeRow = sheet.getRow(j + 1);
							//Row durationRow = sheet.getRow(j + 5);
							System.out.println("Days::\t");
							System.out.print("In Time::\t");
							for (int k1 = 2; k1 <= inTimeRow.getLastCellNum() - 1; k1++) {
								attendence = new EmployeeAttendence();
								String inTimeStr = inTimeRow.getCell(k1).toString();
								String outTimeStr = outTimeRow.getCell(k1).toString();
								//String durationStr = durationRow.getCell(k1).toString();
								String stringdate = dayRow.getCell(k1).toString();
								if (null != inTimeStr && !inTimeStr.trim().equals("")) {
									attendence.setInTime(inTimeStr);
									attendence.setOutTime(outTimeStr);
								//	attendence.setDuration(durationStr);
									
									DateFormat dateFormat= new SimpleDateFormat("dd-MMM-yyyy");
									Date date1 = (Date)dateFormat.parse(stringdate+"-"+Calendar.getInstance().get(Calendar.YEAR));
									Calendar cal = Calendar.getInstance();
									cal.setTime(date1);
									String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.YEAR));
									attendence.setDate(date1);
									attendencelist.add(attendence);
								}
							}
							System.out.println();
						} else if (cells.trim().equals("Status")) {
							i = j;
							break;
						}
					}
					if (null != attendencelist) {
						employee.setAttendanceList(new HashSet<EmployeeAttendence>(attendencelist));
					}
					employeeMap.put(employee.getCode(), employee);
				}
			}
			System.out.println(employeeMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != workSheet)
				workSheet.close();
		}
		return employeeMap;
	}
public static EmployeeAttendence getdbdata(Integer code1, Date date2) {
		String str1 = "select e from EmployeeAttendence e where employee_code=:code and date=:date";
		EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
		TypedQuery<EmployeeAttendence> query = entityManager.createQuery(str1, EmployeeAttendence.class);
		query.setParameter("code", code1);
		query.setParameter("date", date2);
		EmployeeAttendence dbAttenObj=null;
		try {
		 dbAttenObj = query.getSingleResult();
		 entityManager.close();
		 return dbAttenObj;
		}
		catch(Exception e)
		{
			
		}
		return null;
}
public static EmployeeAttendence checktimes(EmployeeAttendence dbAttenObj, EmployeeAttendence fileAttenObj,Employee employee) {
		Integer code=employee.getCode();
		Date date = dbAttenObj.getDate();
		String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = new Date();
			try {
				date1 = dateFormat.parse(dateString);//+"-"+Calendar.getInstance().get(Calendar.YEAR));
				Calendar cal = Calendar.getInstance();
				cal.setTime(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dbAttenObj.setDate(date1);
			Date dbdate = dbAttenObj.getDate();
		String dbinTime=dbAttenObj.getInTime();
		String dboutTime=dbAttenObj.getOutTime();
		String[] dbinTimesplit = dbinTime.split(":");
		int inTimedbfirstHour = Integer.parseInt(dbinTimesplit[0]);
		int inTimedbfirstMinute = Integer.parseInt(dbinTimesplit[1]);
		String[] dboutTimesplit = dboutTime.split(":");
		int outtimedbfirstHour = Integer.parseInt(dboutTimesplit[0]);
		int outtimedbfirstMinute = Integer.parseInt(dboutTimesplit[1]);
		
		
		Integer code2=employee.getCode();
			Date exceldate = fileAttenObj.getDate();
			String excelinTime = fileAttenObj.getInTime();
			String exceloutTime = fileAttenObj.getOutTime();
			String[] excelinTimesplit = excelinTime.split(":");
			int inTimexcelfirstHour = Integer.parseInt(excelinTimesplit[0]);
			int inTimexcelfirstMinute = Integer.parseInt(excelinTimesplit[1]);
			String[] exceloutTimesplit = exceloutTime.split(":");
			int outtimexcelfirstHour = Integer.parseInt(exceloutTimesplit[0]);
			int outtimexcelfirstMinute = Integer.parseInt(exceloutTimesplit[1]);

			if ((dbdate.equals(exceldate)) && (code.equals(code2))) {
				if (((inTimedbfirstHour == 0 && inTimedbfirstMinute == 0)
						&& (inTimexcelfirstHour == 0 && inTimexcelfirstMinute == 0))
						&& ((outtimedbfirstHour == 0 && outtimedbfirstMinute == 0)
								&& (outtimexcelfirstHour == 0 && outtimexcelfirstMinute == 0))) {
					
					employee.setCode(code);
					fileAttenObj.setInTime(dbinTime);
					fileAttenObj.setOutTime(dboutTime);
				} else if (((inTimedbfirstHour == 0 && inTimedbfirstMinute == 0)
						&& (inTimexcelfirstHour < 24 && inTimexcelfirstMinute < 60))
						&& ((outtimedbfirstHour == 0 && outtimedbfirstMinute == 0)
								&& (outtimexcelfirstHour < 24 && outtimexcelfirstMinute < 60))) {
					employee.setCode(code);
					fileAttenObj.setInTime(excelinTime);
					fileAttenObj.setOutTime(exceloutTime);
				} else if (((inTimedbfirstHour < 24 && inTimedbfirstMinute < 60)
						&& (inTimexcelfirstHour == 0 && inTimexcelfirstMinute == 0))
						&& ((outtimedbfirstHour < 24 && outtimedbfirstMinute < 60)
								&& (outtimexcelfirstHour == 0 && outtimexcelfirstMinute == 0))) {
					employee.setCode(code);
					fileAttenObj.setInTime(dbinTime);
					fileAttenObj.setOutTime(dboutTime);

				} else if (((inTimedbfirstHour < 24 && inTimedbfirstMinute < 60)
						&& (inTimexcelfirstHour < 24 && inTimexcelfirstMinute < 60))
						&& ((outtimedbfirstHour < 24 && outtimedbfirstMinute < 60)
								&& (outtimexcelfirstHour < 24 && outtimexcelfirstMinute < 60))) {

					if (inTimedbfirstHour <= inTimexcelfirstHour) {
						employee.setCode(code);
						fileAttenObj.setInTime(dbinTime);
					} else {
						employee.setCode(code);
						fileAttenObj.setInTime(excelinTime);
					}
					if (outtimedbfirstHour > outtimexcelfirstHour) {
						employee.setCode(code);
						fileAttenObj.setOutTime(dboutTime);
					} else {
						employee.setCode(code);
						fileAttenObj.setOutTime(exceloutTime);
					}
				}
				fileAttenObj.setId(dbAttenObj.getId());
				fileAttenObj.setEmployee(dbAttenObj.getEmployee());
				}
		return fileAttenObj;
	}
		
}
