package dev.felnull.imp;

import com.google.common.base.Suppliers;

import java.util.Calendar;
import java.util.function.Supplier;

public class IMPHoliday {
    private static final Supplier<Boolean> XMAS = Suppliers.memoize(() -> {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26;
    });

    public static boolean isXmas() {
        return XMAS.get();
    }
}
