package is.fistlab.database.repositories;

import is.fistlab.database.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PersonRepository extends
        JpaRepository<Person, Long>,
        JpaSpecificationExecutor<Person> {

    @NonNull
    Page<Person> findAll(@NonNull Pageable pageable);

    @Query(
         value = "SELECT get_height_sum()",
            nativeQuery = true
    )
    Integer getSumHeight();

    @Query(
            value = "SELECT get_unique_heights()",
            nativeQuery = true
    )
    List<Double> getUniqueHeight();

    @Query(
            value = "SELECT get_hair_color_percentage(:hair_color_arg)"
    )
    Double getPercent(String hair_color_arg);

    @Query(value = "SELECT * FROM get_start_with(:prefix)", nativeQuery = true)
    List<Person> findByNameStartWith(@Param("prefix") String prefix);


    @Query(
            value = "SELECT get_people_count_by_hair_color_in_location(:color, :x, :y, :z)",
            nativeQuery = true
    )
    Integer getCountByColorAndLocation(String color, Integer x, Long y, Float z);

}
