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
		// �������ö���
		Configuration config = new Configuration().configure();
		// ��������ע�����
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
				.buildServiceRegistry();
		// �����Ự��������
		sessionFactory = config.buildSessionFactory(serviceRegistry);
		// �����Ự����
		session = sessionFactory.openSession();
		// ��������
		transaction = session.beginTransaction();
	}

	@After
	public void destroy() {
		transaction.commit();// �ύ����
		session.close();// �رջỰ
		sessionFactory.close();// �رջỰ����
	}

	@Test
	public void testOneToMany() {
		Grade grade = new Grade("һ��", "Java�����");
		Student student1 = new Student("����", "��");
		Student student2 = new Student("Ů��", "Ů");

		// ���ϣ����ѧ��������Ӷ�Ӧ�İ༶��ţ���Ҫ�ڰ༶�����ѧ��������������ϵ
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
		Grade grade = new Grade("JAVA����", "JAVA�����������");
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
