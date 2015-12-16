#import "DriversAroundNotification.h"

@implementation DriversAroundNotification

@synthesize locations;

- (void) init {
  if (self = [super init]) {
    locations = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [locations release];
  [super dealloc];
}

@end
