package org.riverframework.core;

/**
 * It is used to manage Documents. It is used if we don't need to create specific classes for each
 * document type.
 *
 * @author mario.sotil@gmail.com
 */
public final class DefaultDocument extends AbstractDocument<DefaultDocument> {

  protected DefaultDocument(Database d, org.riverframework.wrapper.Document<?> _d) {
    super(d, _d);
  }

  @Override
  protected DefaultDocument getThis() {
    return this;
  }
}
