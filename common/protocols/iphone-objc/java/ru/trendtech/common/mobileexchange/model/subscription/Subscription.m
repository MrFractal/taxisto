#import "Subscription.h"

@implementation Subscription

@synthesize starting;

- (void) init {
  if (self = [super init]) {
    starting = YES;
  }
  return self;
}

@end
