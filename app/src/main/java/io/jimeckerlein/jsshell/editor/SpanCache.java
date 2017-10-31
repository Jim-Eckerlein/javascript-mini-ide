package io.jimeckerlein.jsshell.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A helper class providing cached spans.
 * Instead of generating a whole new span set each time the highlighter runs, this class is able to
 * cache the spans from the previous highlighter invocation, and only creates new ones if the highlighter needs more.
 * <p>
 * The generator callback is responsible for span creation. Since the time of generation is *not* obvious to the user,
 * and also not manipulable, the generator should always return the same type of span.
 *
 * @param <T> Type of spans to be created.
 */
public class SpanCache<T> {

    private static final int RESIZE_AMOUNT = 20;

    private final Generator<T> mGenerator;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<T> mList = new ArrayList<>();
    private ListIterator<T> mIter = mList.listIterator();

    public SpanCache(Generator<T> generator) {
        mGenerator = generator;
    }

    /**
     * Return a new span.
     * @return New span
     */
    public synchronized T generate() {
        if(!mIter.hasNext()) {
            bunchCreate();
        }
        return mIter.next();
    }

    /**
     * Call this if you do not longer need the span objects returned so far.
     */
    public void reset() {
        mIter = mList.listIterator();
    }

    /**
     * Create a new bunch of spans.
     * Called when the user requested a new span, but buffer ran out of spans.
     */
    private void bunchCreate() {
        for(int i = 0; i < RESIZE_AMOUNT; i++) {
            mIter.add(mGenerator.run());
            mIter.previous();
        }
    }

    public interface Generator<T> {
        T run();
    }

}
