#import "StartTripResponse.h"

@implementation StartTripResponse

@synthesize started;

- (void) init {
  if (self = [super init]) {
    started = NO;
  }
  return self;
}

@end
