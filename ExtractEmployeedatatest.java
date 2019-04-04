package omniwyse.read;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

class ExtractEmployeedatatest {
	
	@Test
	void testfordbdata() throws InvalidFormatException, IOException {
		Employee emp=new Employee();
		emp.setCode(2);
		emp.setName("Siva Krishna Kandagaddla");
		HashMap<Integer, Employee> extract = ExtractEmployeeDataFromExcel.extract();
		for (Entry<Integer, Employee> strKey : extract.entrySet()) {
			Employee employee = new Employee(strKey.getValue().getName(), strKey.getValue().getCode(),strKey.getValue().getAttendanceList());
			employee.getAttendanceList().forEach(fileAttenObj -> {
				Integer code1 = employee.getCode();
				Date date2 = fileAttenObj.getDate();
		EmployeeAttendence dbAttenObj = ExtractEmployeeDataFromExcel.getdbdata(code1,date2);
		if((emp.getCode()).equals(dbAttenObj.getEmployee().getCode())){
			assertEquals(emp.getCode(),dbAttenObj.getEmployee().getCode());
			assertEquals(emp.getName(),dbAttenObj.getEmployee().getName());
		}
		});
		}
	}

	@Test
	void testvaluesforall() throws InvalidFormatException, IOException, ParseException {
		Employee emp=new Employee();
		emp.setCode(2);
		emp.setName("Siva Krishna Kandagaddla");
		Set<EmployeeAttendence> List = new HashSet<EmployeeAttendence>();	
		EmployeeAttendence attendence = new EmployeeAttendence();
		String string = "01-Feb-2019";
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		Date date = format.parse(string);
		attendence.setDate(date);
		attendence.setInTime("09:27:07");
		attendence.setOutTime("21:57:33");
		List.add(attendence);
		emp.setAttendanceList(List);
		
		HashMap<Integer, Employee> extract = ExtractEmployeeDataFromExcel.extract();
		for (Entry<Integer, Employee> strKey : extract.entrySet()) {
			Employee employee = new Employee(strKey.getValue().getName(), strKey.getValue().getCode(),strKey.getValue().getAttendanceList());
			employee.getAttendanceList().forEach(fileAttenObj -> {
				Integer code1 = employee.getCode();
				Date date2 = fileAttenObj.getDate();
				EmployeeAttendence dbAttenObj = ExtractEmployeeDataFromExcel.getdbdata(code1,date2);
				if((date.equals(date2))&&(emp.getCode().equals(code1))) {
				EmployeeAttendence checktimes = ExtractEmployeeDataFromExcel.checktimes(dbAttenObj,fileAttenObj,employee);
				checktimes.getEmployee().getCode();
				assertEquals(emp.getCode(),checktimes.getEmployee().getCode());
				assertEquals(attendence.getInTime(),checktimes.getInTime());
				assertEquals(attendence.getOutTime(),checktimes.getOutTime());
				assertEquals(emp.getName(),checktimes.getEmployee().getName());
				}
		});
		}
	
	}

}
