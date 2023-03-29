import 'package:json_annotation/json_annotation.dart';

part 'city.g.dart';


@JsonSerializable()
class City extends Object {

  @JsonKey(name: 'name')
  String name;

  City(this.name,);

  factory City.fromJson(Map<String, dynamic> srcJson) => _$CityFromJson(srcJson);

  Map<String, dynamic> toJson() => _$CityToJson(this);

}


