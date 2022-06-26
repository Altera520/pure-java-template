package org.altera.util;

import org.altera.model.CarInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.altera.util.SqlUtil.*;

public class SqlUtilTest {

    @Test
    public void select_car_info() throws SQLException, IOException {
        // given
        Function<ResultSet, Optional<CarInfo>> bind = uncheck(rs -> new CarInfo(
            rs.getString("vin"),
            rs.getString("corp"),
            rs.getString("model"),
            rs.getString("model_year"),
            rs.getString("owner"),
            rs.getString("gender")
        ));
        String vin = "137FA90341E593763";

        // when
        CarInfo carInfo1 = selectFile("sql/select.car_info.sql", bind)
            .stream().filter(row -> row.getVin().equals(vin)).collect(Collectors.toList()).get(0);
        String sql = "select vin, corp, model, model_year, owner, gender from car_info where vin = ${vin}";
        CarInfo carInfo2 = select(sql, bind, Pair.of("vin", vin)).get(0);

        // then
        Assertions.assertEquals(carInfo1, carInfo2);
    }

    @Test
    public void env_test() {
        System.out.println(System.getenv("APP_MODE"));
        System.out.println(System.getenv("APP_MODE") == null);
    }
}
