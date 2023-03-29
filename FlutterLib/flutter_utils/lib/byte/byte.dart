

class Byte {

  int _byte = 0;

  int get value => _byte;

  Byte(int value) {
    _byte = value & 0xFF;
  }

  @override
  String toString() {
    return '0x' + (_byte.toRadixString(16).padLeft(2, '0').toUpperCase());
  }
}
