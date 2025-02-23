package com.loy124.myapp.member.controller;

import com.loy124.myapp.AbstractRestDocsTests;
import jakarta.transaction.Transactional;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


//@WebMvcTest(value = MemberController.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest extends AbstractRestDocsTests {



}