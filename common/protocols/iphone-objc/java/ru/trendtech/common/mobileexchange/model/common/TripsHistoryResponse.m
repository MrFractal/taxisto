#import "TripsHistoryResponse.h"

@implementation TripsHistoryResponse

@synthesize history;

- (void) init {
  if (self = [super init]) {
    history = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [history release];
  [super dealloc];
}

@end
