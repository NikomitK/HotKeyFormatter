package de.nikomitk.hotkeyformatter;

import lombok.Getter;
import lombok.Setter;
import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.output.FileOutput;
import yapion.serializing.YAPIONSerializer;
import java.io.File;
import java.io.IOException;

@YAPIONData
@Getter
@Setter
public class Storage {

    private int [] hotkeyButtons = {29,16};

    public void save() {
        try {
            YAPIONSerializer.serialize(this).toYAPION(new FileOutput(new File("storage.yapion")));
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

}
