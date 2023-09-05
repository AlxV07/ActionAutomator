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
        String template_path = "./src/main/java/Gui/TextEditorGui/template.py";
        String old_content = Files.readString(Path.of(template_path));
        String[] c = old_content.split("start", 2);

        StringBuilder code = new StringBuilder();
        for (String s : codeLines) {
            code.append("\t");
            code.append(s);
            code.append("\n");
        }
        String new_content = c[0] + code + c[1];
        String run_path = "./src/main/java/Gui/TextEditorGui/runner.py";
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
