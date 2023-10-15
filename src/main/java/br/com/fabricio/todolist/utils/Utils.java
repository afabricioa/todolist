package br.com.fabricio.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    public static void copyNullProperties(Object source, Object target){
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source); //interface que fornece uma forma de acessar as propriedades de um objeto
        //source é o objecto que veio da requisição, verifica quai os valores o usuario colocou pra alterar
        
        PropertyDescriptor[] pds = src.getPropertyDescriptors(); //pega as propriedades do objeto

        Set<String> emptyNames = new HashSet<>(); //cria um array de valores unicos

        for(PropertyDescriptor pd : pds) { //faz iteração entre as keys/propriedades do objeto
            Object srcValue = src.getPropertyValue(pd.getName()); //pega o valor de cada propriedade do objeto.
            if(srcValue == null){ //se o valor for null adiciona no array que verifica quais valores sao nulos
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
