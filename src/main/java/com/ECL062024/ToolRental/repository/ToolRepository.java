package com.ECL062024.ToolRental.repository;

import com.ECL062024.ToolRental.model.Tool;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ToolRepository {

    private static final Map<String, Tool> tools = new HashMap<>();

    static {
        tools.put("CHNS", new Tool(
                "CHNS",
                "Chainsaw",
                "Stihl",
                1.49,
                true,
                false,
                true));

        tools.put("LADW", new Tool(
                "LADW",
                "Ladder",
                "Werner",
                1.99,
                true,
                true,
                false));

        tools.put("JAKD", new Tool(
                "JAKD",
                "Jackhammer",
                "DeWalt",
                2.99,
                true,
                false,
                false));

        tools.put("JAKR", new Tool(
                "JAKR",
                "Jackhammer",
                "Ridgid",
                2.99,
                true,
                false,
                false));
    }

    private ToolRepository() {
        // Private constructor to prevent instantiation
    }

    public static Tool findByCode(String toolCode) {
        return tools.get(toolCode);
    }
}
