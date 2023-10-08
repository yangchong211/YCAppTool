/**
 * Copyright 2015 - 2016 KeepSafe Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.getkeepsafe.relinker.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Elf64Header extends Elf.Header {
    private final ElfParser parser;

    public Elf64Header(final boolean bigEndian, final ElfParser parser) throws IOException {
        this.bigEndian = bigEndian;
        this.parser = parser;

        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        type = parser.readHalf(buffer, 0x10);
        phoff = parser.readLong(buffer, 0x20);
        shoff = parser.readLong(buffer, 0x28);
        phentsize = parser.readHalf(buffer, 0x36);
        phnum = parser.readHalf(buffer, 0x38);
        shentsize = parser.readHalf(buffer, 0x3A);
        shnum = parser.readHalf(buffer, 0x3C);
        shstrndx = parser.readHalf(buffer, 0x3E);
    }

    @Override
    public Elf.SectionHeader getSectionHeader(final int index) throws IOException {
        return new Section64Header(parser, this, index);
    }

    @Override
    public Elf.ProgramHeader getProgramHeader(final long index) throws IOException {
        return new Program64Header(parser, this, index);
    }

    @Override
    public Elf.DynamicStructure getDynamicStructure(final long baseOffset, final int index)
            throws IOException {
        return new Dynamic64Structure(parser, this, baseOffset, index);
    }
}
