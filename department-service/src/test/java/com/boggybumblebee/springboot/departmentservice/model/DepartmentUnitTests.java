package com.boggybumblebee.springboot.departmentservice.model;

import com.boggybumblebee.springboot.common.model.AbstractBeanTest;
import org.junit.jupiter.api.Test;

public class DepartmentUnitTests extends AbstractBeanTest {

    private static final Class<?> CLASS_UNDER_TEST = Department.class;

    @Override
    @Test
    public void testPojoContractMet() {
        super.assertMeetsPojoContract(CLASS_UNDER_TEST);
    }

    @Override
    @Test
    public void testEqualsContractMet() {
        super.assertMeetsEqualsContract(CLASS_UNDER_TEST);
    }

    @Override
    @Test
    public void testHashCodeContractMet() {
        super.assertMeetsHashCodeContract(CLASS_UNDER_TEST);
    }

    @Override
    @Test
    public void testToStringContractMet() {
        super.assertToStringContract(CLASS_UNDER_TEST);
    }
}
