package com.github.mdcdi1315.mdex.dco_logic;

/**
 * For classes that can compress or transform data to a binary form.
 */
public interface Compilable
{
    /**
     * Compiles the data that can be compiled.
     */
    void Compile();

    /**
     * Returns a value whether compilation has been succeeded.
     * @return true, if the data could be compiled for this object; otherwise, false.
     */
    boolean IsCompiled();
}