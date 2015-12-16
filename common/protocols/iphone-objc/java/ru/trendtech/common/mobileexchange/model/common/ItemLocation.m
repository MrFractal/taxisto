#import "ItemLocation.h"

@implementation ItemLocation

@synthesize driverId;
@synthesize latitude;
@synthesize longitude;
@synthesize occupied;

- (void) init {
  if (self = [super init]) {
    la = 0;
    lo = 0;
    o = NO;
  }
  return self;
}

@end
