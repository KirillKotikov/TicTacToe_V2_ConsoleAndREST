package ru.kotikov.gamexo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.kotikov.gamexo.parsers.JsonParser;
import ru.kotikov.gamexo.parsers.XmlParser;
import ru.kotikov.gamexo.services.FileService;

import java.io.File;

@Controller
public class ReadFileController {

    // обработка файла и отображение истории игры в браузере
    @PostMapping("/print-file")
    public String printFile(@RequestParam("file") MultipartFile file, Model model) {

        String gameHistory = "";
        String tittle = "История игры из файла:";
        if (!file.isEmpty()) {
            File inFile = FileService.convert(file);
            if (file.getOriginalFilename().endsWith(".xml")) {
                gameHistory = new XmlParser().print(inFile);
            } else if (file.getOriginalFilename().endsWith(".json")) {
                gameHistory = new JsonParser().print(inFile);
            }
            if (gameHistory.equals("")) {
                tittle = "";
                gameHistory = "Вы вставили файл без истории игры или неправильно структурированный - " + file.getOriginalFilename() + ".";
            }
        } else {
            return "Вам не удалось загрузить " + file.getOriginalFilename() + ", файл пустой.";
        }
        String[] result = gameHistory.split("\n");
        model.addAttribute("result", result);
        model.addAttribute("tittle", tittle);
        return "printFile";
    }

    @GetMapping("/print-file")
    public String printPage() {
        return "printFile";
    }
}
