package duke.util;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ISerializable {
    void serialize(DataOutputStream dOut) throws IOException;
}