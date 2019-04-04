package omniwyse.read;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class InsertEmployeeDataIntoDatabase {
	public static void main(String[] args) throws InvalidFormatException, IOException {
		HashMap<Integer, Employee> extract = ExtractEmployeeDataFromExcel.extract();
		 EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		for (Entry<Integer, Employee> strKey : extract.entrySet()) {
			Employee employee = new Employee(strKey.getValue().getName(), strKey.getValue().getCode(),strKey.getValue().getAttendanceList());
			System.out.println(employee.getCode() + "------------------------------------- " + employee.getAttendanceList());
			Set<EmployeeAttendence> attendanceList = new HashSet<>();
			employee.getAttendanceList().forEach(fileAttenObj -> {
				Integer code1 = employee.getCode();
				Date date2 = fileAttenObj.getDate();
				EmployeeAttendence dbAttenObj = ExtractEmployeeDataFromExcel.getdbdata(code1,date2);
			if(dbAttenObj==null) {
				attendanceList.add(fileAttenObj);
			}
			else {
				
				EmployeeAttendence checktimes = ExtractEmployeeDataFromExcel.checktimes(dbAttenObj,fileAttenObj,employee);
				attendanceList.add(fileAttenObj);
			}
			});
			if (null != attendanceList) {
			employee.setAttendanceList(attendanceList);     
			}  	
			entityManager.merge(employee);
	
			}		
		transaction.commit();                    
		entityManager.close();		 
	}	
}


