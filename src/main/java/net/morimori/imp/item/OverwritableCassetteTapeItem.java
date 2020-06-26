package net.morimori.imp.item;

public class OverwritableCassetteTapeItem extends CassetteTapeItem {

    public OverwritableCassetteTapeItem(Properties properties) {
        super(properties);

    }

    @Override
    public boolean canOverwrite() {

        return true;
    }
}
