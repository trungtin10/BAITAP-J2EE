package KT_Giuaki.KT.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên học phần không được để trống")
    @Column(nullable = false)
    private String name;

    private String image;

    @Min(value = 1, message = "Số tín chỉ phải lớn hơn 0")
    @Column(nullable = false)
    private Integer credits;

    @NotBlank(message = "Tên giảng viên không được để trống")
    @Column(nullable = false)
    private String lecturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();
}