package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"datajpa", "datajpa,jpa"})
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
}
