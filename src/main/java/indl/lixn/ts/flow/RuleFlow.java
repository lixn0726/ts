package indl.lixn.ts.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 20:46
 **/
public class RuleFlow<T> {

    private final T value;

    private List<Rule<T>> rules = new ArrayList<>();

    private RuleFlow(T value) {
        this.value = value;
    }

    public static <T> RuleFlow<T> of(T value) {
        return new RuleFlow<>(value);
    }

    public RuleFlow<T> join(boolean flag, Consumer<T> consumer) {
        rules.add(Rule.boole(flag, consumer));
        return this;
    }

    public RuleFlow<T> join(Supplier<Boolean> flag, Consumer<T> consumer) {
        rules.add(Rule.func(flag, consumer));
        return this;
    }

    public T exe() {
        for (Rule<T> rule : rules) {
            if (out(rule)) {
                break;
            }
        }
        return value;
    }

    public T exe(Consumer<T> consumer) {
        boolean flag = false;
        for (Rule<T> rule : rules) {
            flag = out(rule);
            if (flag) {
                break;
            }
        }
        if (!flag) {
            consumer.accept(value);
        }
        return value;
    }

    private boolean out(Rule<T> rule) {
        boolean flag;
        switch (rule.getTask()) {
            case FUNC:
                flag = rule.getSupplier().get();
                break;
            case BOOLEAN:
                flag = rule.getFlag();
                break;
            default:
                return false;
        }
        if (flag) {
            rule.getConsumer().accept(value);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
