package com.example.githubapi.controller;

import com.example.githubapi.jsonconnect.Connect;
import com.example.githubapi.jsonconnect.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class GitHubController {
    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/{name}")
    public String info(@PathVariable String name, Model model, HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        Message message = new Message();
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            try {
                String apiUrl = "https://api.github.com/users/name/repos";
                String realUrl = apiUrl.replace("name", name);
                Connect connect = new Connect();
                String response = connect.sendGetRequest(realUrl);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response);

                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                for (JsonNode repoNode : jsonNode) {
                    String ownerLogin = repoNode.get("owner").get("login").asText();
                    jsonObject.put("owner", ownerLogin);
                    if (!repoNode.get("fork").asBoolean()) {
                        JSONObject repoObject = new JSONObject();
                        String repositoryName = repoNode.get("name").asText();
                        String defaultBranch = repoNode.get("default_branch").asText();
                        String lastCommitSHA = repoNode.get("pushed_at").asText();
                        repoObject.put("name", repositoryName);
                        repoObject.put("default_branch", defaultBranch);
                        repoObject.put("last_sha", lastCommitSHA);
                        jsonArray.put(repoObject);
                    }
                }

                jsonObject.put("repository", jsonArray);
                model.addAttribute("json", jsonObject.toString());
                String jsonString = jsonObject.toString();
                System.out.println(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                message.setCode("404");
                message.setWhy("User not found");
                model.addAttribute("error", message);
            }
        } else {
            message.setCode("406");
            message.setWhy("You have not accepted the json application");
            model.addAttribute("error", message);
        }
        return "info";
    }
}
