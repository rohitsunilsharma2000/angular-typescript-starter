package com.example.classroom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.classroom.model.Classroom;
import com.example.classroom.model.Period;
import com.example.classroom.model.Role;
import com.example.classroom.model.User;
import com.example.classroom.repo.ClassroomRepo;
import com.example.classroom.repo.PeriodRepo;
import com.example.classroom.repo.UserRepo;

@SpringBootApplication
public class ClassroomApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClassroomApplication.class, args);
  }

  @Bean
  CommandLineRunner seed(UserRepo userRepo, ClassroomRepo classroomRepo, PeriodRepo periodRepo) {
    return args -> {
      userRepo.save(User.builder().id("p1").role(Role.PRINCIPAL).name("Principal")
          .email("principal@classroom.com").password("Admin").build());

      classroomRepo.save(Classroom.builder().id("c1").name("Classroom 1")
          .dayWindows("Mon=12:00-18:00;Tue=12:00-18:00;Wed=12:00-18:00;Thu=12:00-18:00;Fri=12:00-18:00;Sat=12:00-16:00")
          .teacherId("t1").build());

      userRepo.save(User.builder().id("t1").role(Role.TEACHER).name("Teacher One")
          .email("t1@classroom.com").password("t1").classroomId("c1").build());
      userRepo.save(User.builder().id("t2").role(Role.TEACHER).name("Teacher Two")
          .email("t2@classroom.com").password("t2").classroomId(null).build());

      userRepo.save(User.builder().id("s1").role(Role.STUDENT).name("Student A")
          .email("s1@classroom.com").password("s1").classroomId("c1").teacherId("t1").build());
      userRepo.save(User.builder().id("s2").role(Role.STUDENT).name("Student B")
          .email("s2@classroom.com").password("s2").classroomId("c1").teacherId("t1").build());
      userRepo.save(User.builder().id("s3").role(Role.STUDENT).name("Student C")
          .email("s3@classroom.com").password("s3").classroomId(null).teacherId(null).build());

      periodRepo.save(Period.builder().id("tt1").classroomId("c1").day("Mon").subject("Math")
          .startTime("12:30").endTime("13:30").build());
      periodRepo.save(Period.builder().id("tt2").classroomId("c1").day("Mon").subject("English")
          .startTime("14:00").endTime("15:00").build());
    };
  }
}
