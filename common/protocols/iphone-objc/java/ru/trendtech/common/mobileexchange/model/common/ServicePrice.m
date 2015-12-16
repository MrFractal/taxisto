#import "ServicePrice.h"

@implementation ServicePrice

@synthesize service;
@synthesize details;
@synthesize cost;

- (void) dealloc {
  [service release];
  [details release];
  [super dealloc];
}

@end
