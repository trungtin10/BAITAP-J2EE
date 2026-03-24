package KT_Giuaki.KT.service;

import KT_Giuaki.KT.entity.Course;
import KT_Giuaki.KT.entity.Enrollment;
import KT_Giuaki.KT.entity.Student;
import KT_Giuaki.KT.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public boolean enroll(Student student, Course course) {
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            return false;
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollmentRepository.save(enrollment);
        return true;
    }

    public List<Enrollment> findByStudent(Student student) {
        return enrollmentRepository.findByStudent(student);
    }

    @Transactional
    public void unenroll(Student student, Course course) {
        enrollmentRepository.findByStudentAndCourse(student, course)
                .ifPresent(enrollmentRepository::delete);
    }
}
