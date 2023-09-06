package Gui.TextEditorGui;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CodeBuilder {
    public List<String> getActionLinesFromCode(List<String> codeLines) throws IOException {
        String currentDir = "/home/alxv05/.action_automator/";
        String templatePath = currentDir + "template.py";
        String oldContent = Files.readString(Path.of(templatePath));
        String[] c = oldContent.split("start", 2);

        StringBuilder code = new StringBuilder();
        for (String s : codeLines) {
            code.append("\t");
            code.append(s);
            code.append("\n");
        }
        String new_content = c[0] + code + c[1];
        String run_path =  currentDir + "runner.py";
        FileWriter writer = new FileWriter(run_path);
        writer.write(new_content);
        writer.close();

        Process p = new ProcessBuilder().command("python3", run_path).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder result = new StringBuilder();
        String l;
        while ((l = reader.readLine()) != null) {
            result.append(l);
        }

        return List.of(result.toString().split("\\}.\\{"));
    }
}
