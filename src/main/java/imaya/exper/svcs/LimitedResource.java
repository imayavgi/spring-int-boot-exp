package imaya.exper.svcs;

public interface LimitedResource {
    public void use() throws InterruptedException;
}
