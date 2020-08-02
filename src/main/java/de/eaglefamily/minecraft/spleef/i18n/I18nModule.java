package de.eaglefamily.minecraft.spleef.i18n;

import com.google.inject.Binder;
import com.google.inject.Module;

public class I18nModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(Translator.class).to(MessageFormatTranslator.class);
  }
}
