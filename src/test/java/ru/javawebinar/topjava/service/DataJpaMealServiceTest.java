package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"datajpa", "datajpa,jpa"})
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
}
