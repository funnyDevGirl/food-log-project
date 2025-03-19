package ru.foodlog.service;

import org.springframework.stereotype.Service;
import ru.foodlog.enums.Gender;
import ru.foodlog.enums.Purpose;
import ru.foodlog.model.User;
import java.util.Objects;
import static java.lang.String.format;

@Service
public class BMRService {

    public static final double MALE_CONSTANT = 88.362;
    public static final double MALE_WEIGHT_COEFFICIENT = 13.397;
    public static final double MALE_HEIGHT_COEFFICIENT = 4.799;
    public static final double MALE_AGE_COEFFICIENT = 5.677;

    public static final double FEMALE_CONSTANT = 447.593;
    public static final double FEMALE_WEIGHT_COEFFICIENT = 9.247;
    public static final double FEMALE_HEIGHT_COEFFICIENT = 3.098;
    public static final double FEMALE_AGE_COEFFICIENT = 4.330;

    public static final double WEIGHT_LOSS_MULTIPLIER = 1.3;
    public static final double MAINTENANCE_MULTIPLIER = 1.6;
    public static final double WEIGHT_GAIN_MULTIPLIER = 1.8;

    /**
     * Метод для расчета ежедневной нормы калорий в зависимости от цели.
     *
     * @param user объект, содержащий информацию о пользователе.
     * @return рассчитанное значение ежедневной нормы калорий.
     */
    public double calculateDailyCalories(User user) {

        double bmr = calculateBMR(user);

        if (Objects.requireNonNull(user.getPurpose()) == Purpose.LOSS_WEIGHT) {
            return bmr * WEIGHT_LOSS_MULTIPLIER;

        } else if (user.getPurpose() == Purpose.MAINTENANCE) {
            return bmr * MAINTENANCE_MULTIPLIER;

        } else if (user.getPurpose() == Purpose.GAIN_WEIGHT) {
            return bmr * WEIGHT_GAIN_MULTIPLIER;
        }

        throw new IllegalArgumentException(format("Unknown purpose: '%s'", user.getPurpose()));
    }

    /**
     * Метод для расчета основного обмена веществ (BMR).
     *
     * @param user объект, содержащий информацию о пользователе.
     * @return рассчитанное значение BMR.
     */
    private double calculateBMR(User user) {

        if (user.getGender() == null) {
            throw new IllegalArgumentException("Gender cannot be empty.");
        }

        if (user.getGender() == Gender.MALE) {
            return calculateBMRForMale(user);

        } else if (user.getGender() == Gender.FEMALE) {
            return calculateBMRForFemale(user);
        }

        throw new IllegalArgumentException(format("Unknown gender value: '%s'", user.getGender()));
    }

    /**
     * Метод для расчета BMR для мужчин.
     *
     * @param user объект, содержащий информацию о пользователе.
     * @return рассчитанное значение BMR для мужчин.
     */
    private double calculateBMRForMale(User user) {

        return MALE_CONSTANT
                + (MALE_WEIGHT_COEFFICIENT * user.getWeight())
                + (MALE_HEIGHT_COEFFICIENT * user.getHeight())
                - (MALE_AGE_COEFFICIENT * user.getAge());
    }

    /**
     * Метод для расчета BMR для женщин.
     *
     * @param user объект, содержащий информацию о пользователе.
     * @return рассчитанное значение BMR для женщин.
     */
    private double calculateBMRForFemale(User user) {

        return FEMALE_CONSTANT
                + (FEMALE_WEIGHT_COEFFICIENT * user.getWeight())
                + (FEMALE_HEIGHT_COEFFICIENT * user.getHeight())
                - (FEMALE_AGE_COEFFICIENT * user.getAge());
    }
}
