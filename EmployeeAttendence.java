package omniwyse.read;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "employee_attendance")
public class EmployeeAttendence {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Integer id;
	
	private Date date;
	
	private String inTime;
	
	private String outTime;

	@ManyToOne
	private Employee employee;

	public EmployeeAttendence(Date date, String inTime, String outTime, Employee employee) {
		super();

		this.date = date;
		this.inTime = inTime;
		this.outTime = outTime;
		this.employee = employee;
	}

	public EmployeeAttendence() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date stringdate) {
		this.date = stringdate;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public static EmployeeAttendence getdbdata() {
		return null;
		
		
	}

	
}
