package ru.virtu.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.virtu.test.dto.GoodsDTO;
import ru.virtu.test.dto.GoodsDTOTest;
import ru.virtu.test.models.Goods;
import ru.virtu.test.services.GoodsService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoodsController.class)
class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsService service;

    List<GoodsDTO> goodsDTOList;
    List<Goods> goodsList;

    GoodsDTO goodsDTO;
    Goods goods;

    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    public GoodsControllerTest() {
        this.goodsDTO = new GoodsDTO(1L, "goodsDTO", 100L);
        this.goodsDTOList = Collections.singletonList(goodsDTO);

        this.goods = new Goods(1L,"goods", 50L);
        this.goodsList = Collections.singletonList(goods);
    }

    @Test
    public void getAllGoodses_Success() throws Exception {

        when(service.findAll()).thenReturn(goodsList);
        this.mockMvc.perform(get("/goods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("goods"))
                .andExpect(jsonPath("$[0].price").value("50"));
    }

    @Test
    public void getGoods_Success() throws Exception {

        when(service.findOne(1L)).thenReturn(goods);
        this.mockMvc.perform(get("/goods/1/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("goods"))
                .andExpect(jsonPath("$.price").value("50"));
    }

    @Test
    public void addGoods_Success() throws Exception {
        GoodsDTOTest goods = new GoodsDTOTest("newName", 100L);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(goods);

        this.mockMvc.perform(post("/goods/add").accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).save(any());
    }

    @Test
    public void updateGoods_Success() throws Exception {
        GoodsDTO goods = new GoodsDTO(1L,"newName", 100L);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(goods);

        this.mockMvc.perform(put("/goods/1/update").accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).update(any(),any());
    }

    @Test
    public void deleteGoods_Success() throws Exception {

        this.mockMvc.perform(delete("/goods/1/delete"))
                .andExpect(status().isOk());

        verify(service).delete(any());
    }

}