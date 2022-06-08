package jp.vmware.sbp.kubereport.controller.spring.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void CheckConstraints(){
        assertEquals("/api/v1/resource/pods",Constants.KUBERNETES.get("pod"));
    }

}