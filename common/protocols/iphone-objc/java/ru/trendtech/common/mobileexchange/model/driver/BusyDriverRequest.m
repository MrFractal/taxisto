#import "BusyDriverRequest.h"

@implementation BusyDriverRequest

@synthesize value;

- (void) init {
  if (self = [super init]) {
    value = YES;
  }
  return self;
}

@end
