package com.yc.jetpack.study.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 第一步：创建实体，也就是表。
 * @Entity 声明这是一个表（实体），主要参数：tableName-表名、foreignKeys-外键、indices-索引。
 */
@Entity(tableName = "word_table")
class Word(
    /**
     * @PrimaryKey    声明该字段主键并可以声明是否自动创建。
     * @Ignore    声明某个字段只是临时用，不存储在数据库中。
     * @Embedded    用于嵌套，里面的字段同样会存储在数据库中。
     * @ColumnInfo    主要用来修改在数据库中的字段名。
     * 注意：不应该写成val格式
     */
    @field:ColumnInfo(name = "word")
    @field:PrimaryKey
    var word: String
)