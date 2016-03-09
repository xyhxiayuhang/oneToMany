import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateUtil {

	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;

	@Before
	public void init() {
		// 创建配置对象
		Configuration config = new Configuration().configure();
		// 创建服务注册对象
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
				.buildServiceRegistry();
		// 创建会话工厂对象
		sessionFactory = config.buildSessionFactory(serviceRegistry);
		// 创建会话对象
		session = sessionFactory.openSession();
		// 开启事务
		transaction = session.beginTransaction();
	}

	@After
	public void destroy() {
		transaction.commit();// 提交事务
		session.close();// 关闭会话
		sessionFactory.close();// 关闭会话工厂
	}

	@Test
	public void testOneToMany() {
		Grade grade = new Grade("一班", "Java软件班");
		Student student1 = new Student("张三", "男");
		Student student2 = new Student("女神", "女");

		// 如果希望在学生表中添加对应的班级编号，需要在班级中添加学生，建立关联关系
		grade.getStudents().add(student1);
		grade.getStudents().add(student2);
		session.save(grade);
		session.save(student1);
		session.save(student2);
	}

	@Test
	public void testFindStudentByGrade() {
		Grade grade = (Grade) session.get(Grade.class, 1);
		System.out.println(grade.getGname() + "," + grade.getGdesc());

		Set<Student> students = grade.getStudents();
		for (Student student : students) {
			System.out.println(student.getSname() + "," + student.getSex());
		}
	}

	@Test
	public void testUpdate() {
		Grade grade = new Grade("JAVA二班", "JAVA软件开发二班");
		Student student = (Student) session.get(Student.class, 1);
		grade.getStudents().add(student);
		session.save(grade);
	}

	@Test
	public void testDelete() {
		Student student = (Student) session.get(Student.class, 2);
		session.delete(student);
	}
}
