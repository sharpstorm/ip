package duke.task.serializer;

import duke.exception.DukeIOException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;
import duke.testutil.StreamUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TaskSerializerTest {
    @Test
    public void testInflate_valid_success() throws IOException, DukeIOException {
        for (int i = 1; i < 4; i++) {
            final int innerI = i;
            byte[] reference = StreamUtils.buildOutputStream((dOut) -> {
                dOut.writeShort(innerI);
                dOut.writeUTF("Test Description 1");
                dOut.writeBoolean(true);
                dOut.writeUTF("2022-12-12T13:00");
            });
            assertNotNull(TaskSerializer.inflate(reference));
        }
    }

    @Test
    public void testInflate_invalidType_dukeExceptionRaised() throws IOException {
        byte[] reference = StreamUtils.buildOutputStream((dOut) -> {
            dOut.writeShort(4);
            dOut.writeUTF("Test Description 1");
            dOut.writeBoolean(true);
            dOut.writeUTF("2022-12-12T13:00");
        });
        try {
            TaskSerializer.inflate(reference);
            fail();
        } catch (DukeIOException ex) {
            assertEquals("Encountered unknown format in database", ex.getMessage());
        }
    }

    @Test
    public void testDeflate_valid_success() throws DukeIOException {
        Task[] tasks = new Task[] {
                new Todo("Test 1"),
                new Deadline("Test 2", LocalDateTime.parse("2022-12-12T12:34")),
                new Event("Test 3", LocalDateTime.parse("2022-12-12T12:34"))
        };

        for (int i = 0; i < 3; i++) {
            assertNotNull(TaskSerializer.deflate(tasks[i]));
        }
    }
}