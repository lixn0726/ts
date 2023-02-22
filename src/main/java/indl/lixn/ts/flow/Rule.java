package indl.lixn.ts.flow;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 20:49
 **/
public class Rule<T> {

    private boolean flag;

    private Supplier<Boolean> supplier;

    private Consumer<T> consumer;

    private TaskType taskType;

    private Rule(Supplier<Boolean> supplier, Consumer<T> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
        this.taskType = TaskType.FUNC;
    }

    private Rule(Boolean flag, Consumer<T> consumer) {
        this.flag = flag;
        this.consumer = consumer;
        this.taskType = TaskType.BOOLEAN;
    }

    public static <T> Rule<T> func(Supplier<Boolean> supplier, Consumer<T> consumer) {
        return new Rule<>(supplier, consumer);
    }

    public static <T> Rule<T> boole(Boolean isFlag, Consumer<T> consumer) {
        return new Rule<>(isFlag, consumer);
    }

    public Boolean getFlag() {
        return flag;
    }

    public Supplier<Boolean> getSupplier() {
        return supplier;
    }

    public Consumer<T> getConsumer() {
        return consumer;
    }

    public TaskType getTask() {
        return taskType;
    }

}
