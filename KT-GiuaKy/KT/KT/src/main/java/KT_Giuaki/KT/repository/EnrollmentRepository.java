package KT_Giuaki.KT.repository;

import KT_Giuaki.KT.entity.Course;
import KT_Giuaki.KT.entity.Enrollment;
import KT_Giuaki.KT.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    boolean existsByStudentAndCourse(Student student, Course course);
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
