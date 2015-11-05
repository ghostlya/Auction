package by.dau.data.repository;

import by.dau.data.entity.Match;
import by.dau.data.entity.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetRepository extends JpaRepository<Set, Long> {
    @Query("select s from Set s where s.match = :match")
    List<Set> findByMatch(@Param("match") Match match);

    @Query("select s from Set s where s.match = :match")
    Set findFirstByOrderByMatchAsc(@Param("match") Match match);

//    Set findFirstByOrderByMatchAsc(Match match);
}
